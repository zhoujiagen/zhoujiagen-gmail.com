SELECT name, SUM(length)
FROM MovieExec, Movies
WHERE `producerC#` = `cert#`
GROUP BY name
HAVING MIN(year) < 1930;