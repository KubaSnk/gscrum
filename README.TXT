!!! DO NOT IMPORT THIS PROJECT VIA INTELLIJ "CHECKOUT FROM GITHUB" OPTION !!!

Project setup instruction:
1. Checkout repository
2. Comment dependencies with groupId equals "org.idea" (3 first dependencies)
3. Create new empty project in IntelliJ and import repository as maven module (select pom.xml file)
4. Change module type in gscrum.iml file from JAVA_MODULE to PLUGIN_MODULE
5. Restart IntelliJ
6. Open module settings and set module and plugin SDK to IDEA IU (you should add IntelliJ installation root directory as SDK - IntelliJ SDK) 
7. Perfom maven install
8. Add new Plugin run configuration, use classpath of gscrum module
9. Perform rebuild project
9. Run plugin!

