package rodriguezgonzalez.control;

public interface EventStoreBuilder {
    void save(String json, String topicName);
}
