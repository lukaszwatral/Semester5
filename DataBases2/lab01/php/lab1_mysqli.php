<?php

$db = mysqli_connect("localhost", "root", "", "my_db");
$res = mysqli_query($db, "SELECT * FROM osoby");

while($row = mysqli_fetch_array($res)){
    echo $row['id'] . '. '. $row['imie']. ' ' . $row['nazwisko'] . "\n";
}

?>
