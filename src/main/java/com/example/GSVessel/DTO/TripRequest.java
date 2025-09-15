import com.example.GSVessel.Model.Ship;
import com.example.GSVessel.Model.TripStockItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TripRequest {
    private LocalDate departureDate;
    private LocalDate returnDate;
    private Double otherExpenses;
    private Ship ship;
    private List<TripStockItem> items; // id del stock + cantidad usada
}