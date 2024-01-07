package rodriguezgonzalez.control;

import rodriguezgonzalez.control.exceptions.StoreException;
import rodriguezgonzalez.model.Lodging;
import rodriguezgonzalez.model.Ubication;

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
    public void saveRecommendations() throws StoreException {
        storer.createTables();
        System.out.println("---Tables Iniciated---");
        storer.clearTables();
    }

    @Override
    public void saveUbicationRecommendation(Ubication ubication) throws StoreException {
        storer.saveUbications(ubication);
    }

    @Override
    public void saveLodgingRecommendation(Lodging lodging) throws StoreException {
        storer.saveLodgings(lodging);
    }
}
