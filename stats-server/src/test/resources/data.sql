INSERT INTO public.hits(app, uri, ip, created)
	VALUES
('TestServiceOne','/events/1', '10.1.1.1', '2024-01-01 00:00:01'),
('TestServiceOne','/events/1', '10.1.1.2', '2024-01-01 00:01:01'),
('TestServiceOne','/events/1', '10.1.1.2', '2024-01-01 00:01:02'),
('TestServiceOne','/events/2', '10.1.1.3', '2024-01-01 00:02:01'),
('TestServiceOne','/events/2', '10.1.1.1', '2024-01-01 00:03:01'),
('TestServiceTwo','/events/3', '10.1.1.1', '2024-01-01 00:04:01'),
('TestServiceTwo','/events/3', '10.1.1.1', '2024-01-01 00:05:01');