<?php
    try{
    $conn = new PDO('mysql:host=localhost;dbname=lab2', 'root', '');
    $conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
    $conn->exec("CREATE TABLE IF NOT EXISTS gracze (id int PRIMARY KEY AUTO_INCREMENT NOT NULL, nickname VARCHAR(255) NOT NULL, tag int NOT NULL)");
    $conn->exec("DELETE FROM gracze");
    $tab = array('adam', 'pawel', 'lukasz', 'sebastian', 'igor', NULL, 'jarek', 'michal', 'jakub', 'darek', 'janek');

    $q1 = $conn->prepare("INSERT INTO gracze (nickname, tag) VALUES (?,?)");
    $conn->beginTransaction();
    foreach ($tab as $el){
        $q1->execute(array($el, rand()));
    }
    $conn->commit();
    $res = $conn->query("SELECT * FROM gracze");
    $rows = $res->fetchAll(PDO::FETCH_ASSOC);
    foreach ($rows as $row){
        echo $row['id'] . ". " . $row['nickname'] . " " . $row['tag'] . "\n";
    }
    } catch(PDOException $e){
        print $e->getMessage();
        die();
    }   
?> 