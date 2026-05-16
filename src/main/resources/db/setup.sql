-- Password: admin
INSERT INTO users(name, email, password, created_at)
VALUES
    ('John Doe', 'john@example.com', '$2a$10$dVLMaN3yEmbXBY1yxhC6WOVZkevCOGbX9mODq.wSVMrgYSgyQaXuW', NOW());