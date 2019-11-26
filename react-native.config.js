const path = require('path');

module.exports = {
  dependency: {
    platforms: {
      ios: { podspecPath: path.join(__dirname, 'react-native-custom-module.podspec') },
      android: {
        packageImportPath: 'import com.dineshmaharjan.custommodule.CustomModulePackage;',
        packageInstance: 'new CustomModulePackage()',
      },
    },
  },
};