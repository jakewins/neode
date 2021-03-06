package org.neo4j.neode.statistics;

import java.util.HashMap;
import java.util.Map;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.tooling.GlobalGraphOperations;

public class GraphStatistics
{

    public static GraphStatistics create( GraphDatabaseService db, String description,
                                          NodeLabelResolver nodeLabelResolver )
    {
        GraphStatistics graphStatistics = new GraphStatistics( description, nodeLabelResolver );
        for ( Node node : GlobalGraphOperations.at( db ).getAllNodes() )
        {
            graphStatistics.add( node );
        }
        return graphStatistics;
    }

    public static GraphStatistics create( GraphDatabaseService db, String description )
    {
        return create( db, description, new NeodeNodeLabelResolver() );
    }

    private final String descripton;
    private final NodeLabelResolver nodeLabelResolver;
    private final Map<String, NodeStatistic> nodeStatistics;

    private GraphStatistics( String descripton, NodeLabelResolver nodeLabelResolver )
    {
        this.descripton = descripton;
        this.nodeLabelResolver = nodeLabelResolver;
        nodeStatistics = new HashMap<String, NodeStatistic>();
    }

    private void add( Node node )
    {
        String label = nodeLabelResolver.labelFor( node );
        if ( label == null )
        {
            label = "_UNKNOWN";
        }

        NodeStatistic nodeStatistic = nodeStatistics.get( label );
        if ( nodeStatistic == null )
        {
            nodeStatistics.put( label, nodeStatistic = new NodeStatistic( label ) );
        }

        nodeStatistic.update( node );
    }

    public Iterable<NodeStatistic> nodeStatistics()
    {
        return nodeStatistics.values();
    }

    public NodeStatistic getNodeStatistic( String label )
    {
        return nodeStatistics.get( label );
    }

    public int totalNodes()
    {
        int total = 0;
        for ( NodeStatistic nodeStatistic : nodeStatistics.values() )
        {
            total += nodeStatistic.count();
        }
        return total;
    }

    public int totalRelationships()
    {
        int total = 0;
        for ( NodeStatistic nodeStatistic : nodeStatistics.values() )
        {
            for ( RelationshipStatistic relationshipStatistic : nodeStatistic.relationshipStatistics() )
            {
                total += relationshipStatistic.outgoing().total();
            }
        }
        return total;
    }

    public Map<String, Integer> totalsPerRelationship()
    {
        Map<String, Integer> totals = new HashMap<String, Integer>();

        for ( NodeStatistic nodeStatistic : nodeStatistics.values() )
        {
            for ( RelationshipStatistic relationshipStatistic : nodeStatistic.relationshipStatistics() )
            {
                String label = relationshipStatistic.label();
                Integer total = totals.get( label );
                if ( total == null )
                {
                    totals.put( label, total = 0 );
                }
                totals.put( label, total + relationshipStatistic.outgoing().total() );
            }
        }

        return totals;
    }

    public String description()
    {
        return descripton;
    }

    public void describeTo( GraphStatisticsFormatter formatter )
    {
        formatter.describe( this );
    }
}
