Project setup instruction:
1. Checkout repo (checkout from GitHub)
2. Setup IntelliJ SDK as project SDK (properties -> Project) 
3. Perform maven clean install on project 
4. Change module type in gscrum.iml file from JAVA_MODULE to PLUGIN_MODULE
5. Restart IntelliJ
6. Remove all org.idea* entries from Module settings -> Modules -> Dependencies
7. Add new Plugin run configuration, use classpath of gscrum module
8. Run plugin!


