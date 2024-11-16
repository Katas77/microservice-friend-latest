package social.network.microservice_friend.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import social.network.microservice_friend.model.Friendship;

public interface FriendshipRepository extends JpaRepository<Friendship, Integer > {

}
