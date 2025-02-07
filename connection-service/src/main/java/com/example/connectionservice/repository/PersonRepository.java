package com.example.connectionservice.repository;

import com.example.connectionservice.entity.Person;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import java.util.List;
import java.util.Optional;

public interface PersonRepository extends Neo4jRepository<Person, Long> {
    Optional<Person> getByName(String name);

    @Query("MATCH (user1:User) -[:CONNECTED_TO]- (user2:User) " +
            "WHERE user1.userId = $userId " +
            "RETURN user2")
    List<Person> getFirstDegreeConnections(Long userId);

    @Query("MATCH (user1:User)-[r:REQUESTED_TO]->(user2:User) " +
            "WHERE user1.userId = $senderId AND user2.userId = $receiverId " +
            "RETURN count(r) > 0")
    boolean connectionRequestExists(Long senderId, Long receiverId);

    @Query("MATCH (user1:User)-[r:CONNECTED_TO]-(user2:User) " +
            "WHERE user1.userId = $senderId AND user2.userId = $receiverId " +
            "RETURN count(r) > 0")
    boolean alreadyConnected(Long senderId, Long receiverId);

    @Query("MATCH (user1:User), (user2:User) " +
            "WHERE user1.userId = $senderId AND user2.userId = $receiverId " +
            "CREATE (user1)-[:REQUESTED_TO]->(user2)")
    void addConnectionRequest(Long senderId, Long receiverId);

    @Query("MATCH (user1:User)-[r:REQUESTED_TO]->(user2:User) " +
            "WHERE user1.userId = $senderId AND user2.userId = $receiverId " +
            "DELETE r " +
            "CREATE (user1)-[:CONNECTED_TO]->(user2)")
    void acceptConnectionRequest(Long senderId, Long receiverId);

    @Query("MATCH (user1:User)-[r:REQUESTED_TO]->(user2:User) " +
            "WHERE user1.userId = $senderId AND user2.userId = $receiverId " +
            "DELETE r")
    void rejectConnectionRequest(Long senderId, Long receiverId);

    @Query("CREATE (user:User {userId: $userId, name: $name})")
    void createPerson(Long userId, String name);

}
