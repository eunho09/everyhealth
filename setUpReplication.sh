#!/bin/bash

echo "Waiting for master database to be ready..."
while ! mysql -h database_write -u root -p1234 -e "SELECT 1" >/dev/null 2>&1; do
    echo "Waiting for master..."
    sleep 3
done

echo "Waiting for replica database to be ready..."
while ! mysql -h database_read -u root -p1234 -e "SELECT 1" >/dev/null 2>&1; do
    echo "Waiting for replica..."
    sleep 3
done

echo "Checking if replication user exists..."
USER_EXISTS=$(mysql -h database_write -u root -p1234 -e "SELECT COUNT(*) FROM mysql.user WHERE user='replication_user';" 2>/dev/null | tail -1)

if [ "$USER_EXISTS" = "0" ]; then
    echo "Creating replication user..."
    mysql -h database_write -u root -p1234 -e "
    CREATE USER 'replication_user'@'%' IDENTIFIED WITH mysql_native_password BY '0000';
    GRANT REPLICATION SLAVE ON *.* TO 'replication_user'@'%';
    FLUSH PRIVILEGES;
    "
else
    echo "Replication user already exists, updating password..."
    mysql -h database_write -u root -p1234 -e "
    ALTER USER 'replication_user'@'%' IDENTIFIED WITH mysql_native_password BY '0000';
    GRANT REPLICATION SLAVE ON *.* TO 'replication_user'@'%';
    FLUSH PRIVILEGES;
    "
fi

echo "Verifying replication user..."
mysql -h database_write -u root -p1234 -e "SELECT user, host, plugin FROM mysql.user WHERE user='replication_user';"

echo "Testing replication user connection..."
if mysql -h database_write -u replication_user -p0000 -e "SELECT 1" >/dev/null 2>&1; then
    echo "Replication user connection successful!"
else
    echo "ERROR: Replication user connection failed!"
    exit 1
fi

echo "Getting master status..."
MASTER_STATUS=$(mysql -h database_write -u root -p1234 -e "SHOW MASTER STATUS;" 2>/dev/null | tail -1)
LOG_FILE=$(echo $MASTER_STATUS | awk '{print $1}')
LOG_POS=$(echo $MASTER_STATUS | awk '{print $2}')

echo "Master status: File=$LOG_FILE, Position=$LOG_POS"

if [ ! -z "$LOG_FILE" ] && [ ! -z "$LOG_POS" ] && [ "$LOG_FILE" != "NULL" ]; then
    echo "Setting up replication..."
    mysql -h database_read -u root -p1234 -e "
    STOP REPLICA;
    RESET REPLICA ALL;
    CHANGE REPLICATION SOURCE TO
    SOURCE_HOST='database_write',
    SOURCE_USER='replication_user',
    SOURCE_PASSWORD='0000',
    SOURCE_LOG_FILE='$LOG_FILE',
    SOURCE_LOG_POS=$LOG_POS,
    SOURCE_SSL=0;
    START REPLICA;
    "

    echo "Checking replication status..."
    mysql -h database_read -u root -p1234 -e "SHOW REPLICA STATUS\G" | grep -E "(Running|Error|Last_Error)"

    echo "Replication setup completed!"
else
    echo "ERROR: Invalid master status!"
    exit 1
fi