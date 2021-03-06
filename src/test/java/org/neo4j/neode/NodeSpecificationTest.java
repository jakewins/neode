package org.neo4j.neode;

import java.util.Collections;

import org.junit.Test;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.PropertyContainer;
import org.neo4j.graphdb.Transaction;
import org.neo4j.neode.properties.Property;
import org.neo4j.neode.test.Db;

import static java.util.Arrays.asList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class NodeSpecificationTest
{
    @Test
    public void shouldCreateNewNode() throws Exception
    {
        // given
        GraphDatabaseService db = Db.impermanentDb();
        Transaction tx = db.beginTx();

        NodeSpecification nodeSpecification = new NodeSpecification( "user", Collections.<Property>emptyList(), db );

        // when
        Node node = nodeSpecification.build( 1 );
        tx.success();
        tx.finish();

        // then
        assertNotNull( node );
        assertEquals( "user", node.getProperty( "_label" ) );
    }

    @Test
    public void shouldApplyPropertiesToNewNode() throws Exception
    {
        // given
        GraphDatabaseService db = Db.impermanentDb();
        Transaction tx = db.beginTx();

        Property property = new Property()
        {
            @Override
            public void setProperty( PropertyContainer propertyContainer, GraphDatabaseService db, String label,
                                     int iteration )
            {
                propertyContainer.setProperty( "myproperty", "value" );
            }
        };
        NodeSpecification nodeSpecification = new NodeSpecification( "user", asList( property ), db );

        // when
        Node node = nodeSpecification.build( 1 );
        tx.success();
        tx.finish();

        // then
        assertEquals( "value", node.getProperty( "myproperty" ) );
    }

}
