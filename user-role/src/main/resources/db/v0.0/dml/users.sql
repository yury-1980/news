INSERT INTO public.users (id, user_name, email, password, role)
VALUES (DEFAULT, 'Вася', 'vasya@example.com', '1', 'SUBSCRIBER'::role),
       (DEFAULT, 'Петя', 'petya@example.com', '2', 'JOURNALIST'::role),
       (DEFAULT, 'Маша', 'masha@example.com', '3', 'SUBSCRIBER'::role),
       (DEFAULT, 'Саша', 'sasha@example.com', '4', 'JOURNALIST'::role),
       (DEFAULT, 'Света', 'sveta@example.com', '5', 'SUBSCRIBER'::role),
       (DEFAULT, 'Юра', 'yura@example.com', '6', 'SUBSCRIBER'::role),
       (DEFAULT, 'Катя', 'katya@example.com', '7', 'SUBSCRIBER'::role),
       (DEFAULT, 'Ира', 'ira@example.com', '8', 'SUBSCRIBER'::role),
       (DEFAULT, 'Вова', 'vova@example.com', '9', 'SUBSCRIBER'::role),
       (DEFAULT, 'Маргарита', 'margarita@example.com', '10', 'ADMIN'::role);