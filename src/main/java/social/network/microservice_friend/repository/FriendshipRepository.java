package social.network.microservice_friend.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import social.network.microservice_friend.model.Friendship;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FriendshipRepository extends JpaRepository<Friendship, UUID> {

    @Query(value = "SELECT COUNT(*) FROM friend_schema.friendship WHERE friendship.account_id_to=?1 AND  friendship.status_between='REQUEST_FROM'", nativeQuery = true)
    Integer countREQUEST_FROM(UUID uuidFrom);

    @Query(value = "SELECT * FROM friend_schema.friendship WHERE (friendship.account_id_to=?1 or friendship.account_id_from=?1) AND (friendship.account_id_to=?2 or friendship.account_id_from=?2)", nativeQuery = true)
    Optional<Friendship> findToAndFrom(UUID uuidTo, UUID uuidFrom);

    @Query(value = "SELECT * FROM friend_schema.friendship WHERE friendship.status_between='FRIEND' AND (friendship.account_id_to=?1 or friendship.account_id_from=?1)", nativeQuery = true)
    List<Friendship> findFRIENDS(UUID uuidFrom);

    @Query(value = "SELECT * FROM friend_schema.friendship WHERE friendship.status_between='BLOCKED' AND (friendship.account_id_to=?1 or friendship.account_id_from=?1)", nativeQuery = true)
    List<Friendship> findsBLOCKED(UUID uuidFrom);

    @Query(value = "SELECT friendship.account_id_from FROM friend_schema.friendship WHERE friendship.status_between='REQUEST_FROM' AND friendship.account_id_to=?1", nativeQuery = true)
    List<UUID> findIdStatusREQUEST_FROM(UUID headerUUID);

    @Query(value = "SELECT friendship.account_id_to FROM friend_schema.friendship WHERE friendship.status_between='SUBSCRIBED' AND friendship.account_id_from=?1", nativeQuery = true)
    List<UUID> findIdStatus_SUBSCRIBED(UUID headerUUID);
    @Query(value = "SELECT friendship.account_id_to FROM friend_schema.friendship WHERE friendship.status_between='REQUEST_FROM' AND friendship.account_id_from=?1", nativeQuery = true)
    List<UUID> findIdStatusREQUEST_TO( UUID uuid);
}
