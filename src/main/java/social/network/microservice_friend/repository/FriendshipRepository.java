package social.network.microservice_friend.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import social.network.microservice_friend.model.Friendship;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FriendshipRepository extends JpaRepository<Friendship, UUID> {


    @Query(value = "SELECT COUNT(*) FROM app_schema.friendship WHERE friendship.account_id_from=? AND friendship.status_between='REQUEST_TO'", nativeQuery = true)
    Integer findAllStatus_between(UUID uuidFrom);


    @Query(value = "SELECT * FROM app_schema.friendship WHERE friendship.account_id_to=?", nativeQuery = true)
    List<Friendship> findAllUudTo(UUID uuid);

    @Query(value = "SELECT * FROM app_schema.friendship WHERE friendship.account_id_to=?1 AND friendship.account_id_from=?2", nativeQuery = true)
    Optional<Friendship> findToAndFrom(UUID uuidTo, UUID uuidFrom);

}
