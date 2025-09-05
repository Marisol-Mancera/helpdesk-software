INSERT INTO topics (name, active) VALUES
('Hardware', TRUE),
('Software', TRUE),
('Redes', TRUE),
('Soporte General', TRUE);

INSERT INTO requests (applicant_name, description, status, created_at, updated_at, topic_id)
VALUES
('María', 'No enciende el PC', 'PENDING', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1),
('Pedro', 'Error al abrir Word', 'PENDING', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 2);
