<?php

$db = new mysqli("localhost", "root", "", "my_db");
$db->query("CREATE TABLE IF NOT EXISTS osoby (id INTEGER PRIMARY KEY AUTO_INCREMENT, imie TEXT, nazwisko TEXT)");
$db->query("INSERT INTO osoby VALUES (null, 'Łukasz', 'Watral')");

$res = $db->query("SELECT * FROM osoby");

while($row = $res->fetch_array()){
    echo $row['id'] . '. '. $row['imie']. ' ' . $row['nazwisko'] . "\n";
}
?>
