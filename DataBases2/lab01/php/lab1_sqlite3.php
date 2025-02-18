<?php
$db = new SQLite3('my.db');

$db->exec("CREATE TABLE IF NOT EXISTS osoby (id INTEGER PRIMARY KEY AUTOINCREMENT, imie TEXT, nazwisko TEXT)");
$db->exec("INSERT INTO osoby (imie, nazwisko) VALUES ('Łukasz', 'Watral')");

$res = $db->query('SELECT * FROM osoby');

while($row = $res->fetchArray()){
    echo $row['id'] . '. '. $row['imie']. ' ' . $row['nazwisko'] . "\n";
}
?>
