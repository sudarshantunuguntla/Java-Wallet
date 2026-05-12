to run mvn application - .\mvnw spring-boot:run


docker command to start the db and redis

docker-compose up

post running : 
-- To query in postgres
    docker exec -it wallet-db psql -U postgres -d walletdb

    docker exec -it wallet-db psql -U postgres
    psql -d walletdb
    \c walletdb

    SELECT * FROM wallet;
    
-- To connect with redis
    docker exec -it wallet-redis redis-cli
    ping