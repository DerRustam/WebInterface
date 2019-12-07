SELECT pf.account_id, pf.software_id, a.e_mail, res.title, res.class_name, pf.message
FROM "Product Feedback" pf 
JOIN "Account" a on pf.account_id = a.account_id 
JOIN (SELECT s.software_id, s.title, sc.class_name FROM "Software" s 
     JOIN "Software Class" sc ON s.class_id = sc.class_id) res 
ON pf.software_id = res.software_id
ORDER BY a.e_mail;