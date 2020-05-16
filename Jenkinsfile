node{
    stage('SCM Checkout'){
        git 'https://github.com/TestLeafPages/LeafOrg_Appium_Framework/'
    }
    stage('Compile-Package'){
        sh 'mvn package'
    }
}
