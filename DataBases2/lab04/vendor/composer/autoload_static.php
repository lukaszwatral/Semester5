<?php

// autoload_static.php @generated by Composer

namespace Composer\Autoload;

class ComposerStaticInit4386adcc0a0fb7d013023b194e605c54
{
    public static $prefixLengthsPsr4 = array (
        'P' => 
        array (
            'Psr\\Log\\' => 8,
            'Psr\\Cache\\' => 10,
        ),
        'L' => 
        array (
            'Lukasz\\Lab4\\' => 12,
        ),
        'D' => 
        array (
            'Doctrine\\Deprecations\\' => 22,
            'Doctrine\\DBAL\\' => 14,
        ),
    );

    public static $prefixDirsPsr4 = array (
        'Psr\\Log\\' => 
        array (
            0 => __DIR__ . '/..' . '/psr/log/src',
        ),
        'Psr\\Cache\\' => 
        array (
            0 => __DIR__ . '/..' . '/psr/cache/src',
        ),
        'Lukasz\\Lab4\\' => 
        array (
            0 => __DIR__ . '/../..' . '/src',
        ),
        'Doctrine\\Deprecations\\' => 
        array (
            0 => __DIR__ . '/..' . '/doctrine/deprecations/lib/Doctrine/Deprecations',
        ),
        'Doctrine\\DBAL\\' => 
        array (
            0 => __DIR__ . '/..' . '/doctrine/dbal/src',
        ),
    );

    public static $classMap = array (
        'Composer\\InstalledVersions' => __DIR__ . '/..' . '/composer/InstalledVersions.php',
    );

    public static function getInitializer(ClassLoader $loader)
    {
        return \Closure::bind(function () use ($loader) {
            $loader->prefixLengthsPsr4 = ComposerStaticInit4386adcc0a0fb7d013023b194e605c54::$prefixLengthsPsr4;
            $loader->prefixDirsPsr4 = ComposerStaticInit4386adcc0a0fb7d013023b194e605c54::$prefixDirsPsr4;
            $loader->classMap = ComposerStaticInit4386adcc0a0fb7d013023b194e605c54::$classMap;

        }, null, ClassLoader::class);
    }
}
