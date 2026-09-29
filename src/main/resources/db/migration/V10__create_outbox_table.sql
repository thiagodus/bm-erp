CREATE TABLE outbox_event (
                              id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                              aggregate_type VARCHAR(50) NOT NULL,
                              aggregate_id VARCHAR(100) NOT NULL,
                              event_type VARCHAR(100) NOT NULL,
                              payload JSONB NOT NULL,
                              status VARCHAR(20) NOT NULL,
                              created_at TIMESTAMP WITH TIME ZONE NOT NULL,
                              processed_at TIMESTAMP WITH TIME ZONE
);

CREATE INDEX idx_outbox_status_created ON outbox_event (status, created_at);