SELECT sc.class_id, sc.class_name, scp.class_name 
FROM "Software Class" sc 
LEFT JOIN "Software Class" scp ON sc.class_parent_id = scp.class_id
WHERE sc.class_name = ?;