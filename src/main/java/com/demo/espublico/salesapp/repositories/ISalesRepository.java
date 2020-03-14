package com.demo.espublico.salesapp.repositories;

import com.demo.espublico.salesapp.entities.ISalesCount;
import com.demo.espublico.salesapp.entities.SalesRegistry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ISalesRepository extends JpaRepository<SalesRegistry, Long>
{
    List<SalesRegistry> findByOrderByOrderIdAsc();

    @Query("SELECT s.region AS columnName, COUNT(s.orderId) AS totalCount "
            + "FROM SalesRegistry AS s GROUP BY  s.region ORDER BY s.region DESC")
    List<ISalesCount> countTotalByRegion();

    @Query("SELECT s.country AS columnName,  COUNT(s.id) AS totalCount "
            + "FROM SalesRegistry AS s GROUP BY  s.country ORDER BY s.country DESC")
    List<ISalesCount> countTotalByCountry();

    @Query("SELECT s.itemType AS columnName, COUNT(s.id) AS totalCount "
            + "FROM SalesRegistry AS s GROUP BY s.itemType ORDER BY s.itemType DESC")
    List<ISalesCount> countTotalByItemType();

    @Query("SELECT s.salesChannel AS columnName,COUNT(s.id) AS totalCount "
            + "FROM SalesRegistry AS s GROUP BY  s.salesChannel ORDER BY  s.salesChannel DESC")
    List<ISalesCount> countTotalBySalesChannel();

    @Query("SELECT s.orderPriority AS columnName, COUNT(s.id) AS totalCount "
            + "FROM SalesRegistry AS s GROUP BY  s.orderPriority ORDER BY  s.orderPriority DESC")
    List<ISalesCount> countTotalByOrderPriority();

}
