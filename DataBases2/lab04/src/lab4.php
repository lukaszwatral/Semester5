<?php
    require_once __DIR__ . '/../vendor/autoload.php';

    use Doctrine\DBAL\DriverManager;
    use Doctrine\DBAL\Schema;

    $params = [
        'dbname' => 'lab4',
        'user' => 'root',
        'password' => '',
        'host' => 'localhost',
        'driver' => 'pdo_mysql'
    ];
    $conn = DriverManager::getConnection($params);

    $queryBuilder1 = $conn->createQueryBuilder();
    $queryBuilder1->select('customerName', 'creditLimit')
    ->from('customers')
    ->where('country = :country')
    ->setParameter('country', 'USA')
    ->orderBy('creditlimit', 'DESC');

    $stmt1 = $queryBuilder1->executeQuery();
    $res1 = $stmt1->fetchAllAssociative();

    foreach ($res1 as $row){
        echo $row['customerName'] . ' ' . $row['creditLimit'] . "\n";
    }

    $queryBuilder2 = $conn->createQueryBuilder();
    $queryBuilder2->select('c.customerNumber', 'c.customerName', 'e.firstName', 'e.lastName')
    ->from('customers', 'c')
    ->innerJoin('c', 'employees', 'e', 'c.salesRepEmployeeNumber = e.employeeNumber');

    $stmt2 = $queryBuilder2->executeQuery();
    $res2 = $stmt2->fetchAllAssociative();

    foreach($res2 as $row){
        echo $row['customerNumber'] . ' ' . $row['customerName'] . ' - ' . $row['firstName'] . ' ' . $row['lastName'] . "\n";
    }

    $tableName = 'tabela';
    $schemaManager = $conn->createSchemaManager();

    if(!$schemaManager->tablesExist([$tableName])){
    $schema = new Schema\Schema();
    $tab = $schema->createTable($tableName);
    $tab->addColumn('id', 'integer');
    $tab->addColumn('napis', 'string', ['length'=>100]);
    $tab->addColumn('liczba', 'integer');
    $tab->setPrimaryKey(['id']);

    $sql = $schema->toSql($conn->getDatabasePlatform());

    $conn->executeStatement($sql[0]);
    }

    $queryBuilder = $conn->createQueryBuilder();
    $queryBuilder->delete($tableName);
    $queryBuilder->executeStatement();

    $tab = array('aaa', 'bbb', 'ccc');
    for($i=1; $i<=3; $i++){
        $queryBuilder->insert('tabela')
       ->setValue('id', '?')
       ->setValue('napis', '?')
       ->setValue('liczba', '?')
       ->setParameter(0, $i)
       ->setParameter(1, $tab[$i-1])
       ->setParameter(2, rand());
        $queryBuilder->executeStatement();
    }

?>