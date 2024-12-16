echo "Starting friend-service..."
docker run -d --name friend2 -p 8087:8087 romakat77/microservice-friend:latest
echo "friend-service running...."
