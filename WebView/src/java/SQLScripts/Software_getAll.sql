SELECT s.software_id, sc.class_name, p.publisher_name, s.title, s.release_date, s.esrb, s.actual_price 
FROM "Software" s 
JOIN "Software Class" sc ON s.class_id =sc.class_id 
JOIN "Publisher" p ON s.publisher_id = p.publisher_id 
ORDER BY s.title;