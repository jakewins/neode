/*
 * Copyright (C) 2012 Neo Technology
 * All rights reserved
 */
package org.neo4j.neode;

public interface SetRelationshipConstraints
{
    TargetNodesSpecification relationshipConstraints( Range cardinality, RelationshipUniqueness relationshipUniqueness );

    TargetNodesSpecification relationshipConstraints( Range cardinality );
}
