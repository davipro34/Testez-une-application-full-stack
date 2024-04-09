INSERT INTO TEACHERS (first_name, last_name)
VALUES ('Margot', 'DELAHAYE'),
       ('Hélène', 'THIERCELIN');

-- Insertion d'un utilisateur Admin avec le mot de passe "test!1234"
INSERT INTO USERS (first_name, last_name, admin, email, password)
VALUES ('Admin', 'Admin', true, 'yoga@studio.com', '$2a$10$.Hsa/ZjUVaHqi0tp9xieMeewrnZxrZ5pQRzddUXE/WjDu2ZThe6Iq');

-- Insertion d'un nouvel utilisateur (participant) avec le mot de passe "password"
INSERT INTO USERS (last_name, first_name, admin, email, password, created_at, updated_at)
VALUES ('Doe', 'John', false, 'jdoe@email.com', '$2b$12$7nRUhkgWkmGhR/FrRRrn4O7chFb8aoGsBrNzp7NTln74o9KbVx.yy', NOW(), NOW());

-- Insertion d'une SESSION
-- Pour cet exemple, nous utilisons '1' comme ID d'enseignant, ce qui suppose que Margot DELAHAYE a l'ID 1
INSERT INTO SESSIONS (name, date, description, teacher_id, created_at, updated_at)
VALUES ('Séance de Yoga', '2024-04-15 10:00:00', 'Une séance de yoga revitalisante pour bien commencer la journée.', 1, NOW(), NOW());

-- Ajout du nouvel utilisateur à la SESSION
-- Vous devrez peut-être ajuster cette requête en fonction des IDs réels après les insertions
INSERT INTO PARTICIPATE (session_id, user_id)
VALUES ((SELECT id FROM SESSIONS ORDER BY id DESC LIMIT 1), (SELECT id FROM USERS ORDER BY id DESC LIMIT 1));
