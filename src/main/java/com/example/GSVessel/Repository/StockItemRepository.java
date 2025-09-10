package com.example.GSVessel.Repository;

import com.example.GSVessel.Model.StockItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockItemRepository extends JpaRepository<StockItem,Long> {
}
