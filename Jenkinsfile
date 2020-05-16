node{
    stage('SCM Checkout'){
        git 'https://github.com/TestLeafPages/LeafOrg_Appium_Framework/'
    }
    stage('Compile-Package'){
        def mvnHome = tool name: 'Default', type: 'maven'
        //sh "${mvnHome}/bin/mvn package"
        env.JAVA_HOME = tool name: 'JAVA_HOME', type: 'jdk'
        bat "\"${mvnHome}\"\\bin\\mvn -B install"
    }
}
