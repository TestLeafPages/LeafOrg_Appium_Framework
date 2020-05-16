node{
    stage('SCM Checkout'){
        git 'https://github.com/TestLeafPages/LeafOrg_Appium_Framework/'
    }
    stage('Compile-Package'){
        def mvnHome = tool name: 'maven-3', type: 'maven'
        sh "${mvnHome}/bin/mvn package"
    }
}
