package rodriguezgonzalez.control;

import rodriguezgonzalez.control.exceptions.StoreException;

import java.sql.SQLException;

public class RecommendationStorer implements RecommendationStore{
    private SQLiteRecommendationStore storer;

    public RecommendationStorer() throws StoreException {
        try {
            this.storer = new SQLiteRecommendationStore();
        } catch (SQLException e) {
            throw new StoreException(e.getMessage());
        }
    }

    @Override
    public void saveRecommendations(EventProcessor processor) throws StoreException {
        System.out.println("Lista de Ubicaciones(tamaño): ");
        System.out.println(processor.getUbications().size());
        System.out.println("Lista de Alojamientos(tamaño): ");
        System.out.println(processor.getLodgings().size());
        storer.createTables();
        storer.clearTables();
        storer.saveUbications(processor.getUbications());
        storer.saveLodgings(processor.getLodgings());
    }
}
