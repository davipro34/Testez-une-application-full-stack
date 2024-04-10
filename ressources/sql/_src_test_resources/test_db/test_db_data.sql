-- Insertion des enseignants
INSERT INTO TEACHERS (first_name, last_name)
VALUES ('Margot', 'DELAHAYE'),
       ('Hélène', 'THIERCELIN');

-- Insertion d'un utilisateur Admin avec le mot de passe "test!1234"
INSERT INTO USERS (first_name, last_name, admin, email, password)
VALUES ('Admin', 'Admin', true, 'yoga@studio.com', '$2a$10$.Hsa/ZjUVaHqi0tp9xieMeewrnZxrZ5pQRzddUXE/WjDu2ZThe6Iq');

-- Insertion de nouveaux utilisateurs (participants) avec leur mot de passe
INSERT INTO USERS (last_name, first_name, admin, email, password, created_at, updated_at)
VALUES ('DOE', 'John', false, 'john@email.com', '$2b$12$7nRUhkgWkmGhR/FrRRrn4O7chFb8aoGsBrNzp7NTln74o9KbVx.yy', NOW(), NOW()),
       ('DOE', 'Jane', false, 'jane@email.com', '$2b$12$7nRUhkgWkmGhR/FrRRrn4O7chFb8aoGsBrNzp7NTln74o9KbVx.yy', NOW(), NOW());

-- Insertion d'une SESSION avec Margot DELAHAYE en utilisant son nom complet
INSERT INTO SESSIONS (name, date, description, teacher_id, created_at, updated_at)
VALUES ('Séance de Yoga matin', '2024-04-15 10:00:00', 'Une séance de yoga revitalisante pour bien commencer la journée.',
        (SELECT id FROM TEACHERS WHERE first_name = 'Margot' AND last_name = 'DELAHAYE' LIMIT 1), NOW(), NOW());

-- Insertion d'une autre SESSION avec Hélène THIERCELIN en utilisant son nom complet
INSERT INTO SESSIONS (name, date, description, teacher_id, created_at, updated_at)
VALUES ('Séance de Yoga soir', '2024-04-15 18:00:00', 'Une séance de yoga détente pour bien finir la journée.',
        (SELECT id FROM TEACHERS WHERE first_name = 'Hélène' AND last_name = 'THIERCELIN' LIMIT 1), NOW(), NOW());

-- Ajout de l'utilisateur Jane DOE à la dernière SESSION par email
INSERT INTO PARTICIPATE (session_id, user_id)
VALUES ((SELECT id FROM SESSIONS ORDER BY id DESC LIMIT 1), (SELECT id FROM USERS WHERE email = 'jane@email.com' LIMIT 1));
