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
    stage('Upload') {

        dir('C:\Users\dell\.jenkins\workspace\Appium_Pipeline_Project\reports'){

            pwd(); //Log current directory

            withAWS(region:'Asia Pacific (Mumbai)',credentials:'013017741753') {

                 def identity=awsIdentity();//Log AWS credentials

                // Upload files from working directory 'dist' in your project workspace
                s3Upload(bucket:"eaforgreports", workingDir:'C:\Users\dell\.jenkins\workspace\Appium_Pipeline_Project\reports\result.html', includePathPattern:'**/*');
            }

        };
    }
}
