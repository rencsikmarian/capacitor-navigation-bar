// swift-tools-version: 5.9
import PackageDescription

let package = Package(
    name: "CapacitorNavigationBar",
    platforms: [.iOS(.v15)],
    products: [
        .library(
            name: "CapacitorNavigationBar",
            targets: ["NavigationBarPlugin"])
    ],
    dependencies: [
        .package(url: "https://github.com/ionic-team/capacitor-swift-pm.git", from: "8.0.0")
    ],
    targets: [
        .target(
            name: "NavigationBarPlugin",
            dependencies: [
                .product(name: "Capacitor", package: "capacitor-swift-pm"),
                .product(name: "Cordova", package: "capacitor-swift-pm")
            ],
            path: "ios/Sources/NavigationBarPlugin"),
        .testTarget(
            name: "NavigationBarPluginTests",
            dependencies: ["NavigationBarPlugin"],
            path: "ios/Tests/NavigationBarPluginTests")
    ]
)
