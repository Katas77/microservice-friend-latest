echo "Starting friend-service..."

docker run -d \
--name friend2 \
--network social-network-net \
--restart always \
 rmi romakat77/microservice-friend:latest
echo "Post-service running..."