node{
    stage('SCM Checkout'){
        git 'https://github.com/TestLeafPages/LeafOrg_Appium_Framework/'
    }
    stage('Compile-Package'){
        def mvnHome = tool name: 'Default', type: 'maven'
        //sh "${mvnHome}/bin/mvn package"
        env.JAVA_HOME = tool 'JDK-1.8'
        bat "\"${mvnHome}\"\\bin\\mvn -B verify"
    }
}
