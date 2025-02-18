<?php
    require_once __DIR__ . '/vendor/autoload.php';

    use Doctrine\DBAL\DriverManager;
    use Doctrine\ORM\EntityManager;
    use Doctrine\ORM\ORMSetup;



    $config = ORMSetup::createAttributeMetadataConfiguration([__DIR__ . '/src'], true);


    $conn = DriverManager::getConnection([
        'dbname' => 'lab7',
        'user' => 'root',
        'password' => '',
        'host' => 'localhost',
        'driver' => 'pdo_mysql'
]);

    $entityManager = new EntityManager($conn, $config);
