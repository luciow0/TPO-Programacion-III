package org.example.tpoprogramacioniii.repository;

import org.example.tpoprogramacioniii.model.Segment;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SegmentRepository extends Neo4jRepository<Segment, String> {
}
