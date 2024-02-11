import { NativeModules, Platform } from 'react-native';

const LINKING_ERROR =
  `The package 'react-native-android-signal-strength' doesn't seem to be linked. Make sure: \n\n` +
  Platform.select({ ios: "- You have run 'pod install'\n", default: '' }) +
  '- You rebuilt the app after installing the package\n' +
  '- You are not using Expo Go\n';

const AndroidSignalStrength = NativeModules.AndroidSignalStrength
  ? NativeModules.AndroidSignalStrength
  : new Proxy(
      {},
      {
        get() {
          throw new Error(LINKING_ERROR);
        },
      }
    );

export function getCurrentSignalStrength(): Promise<number> {
  return AndroidSignalStrength.getCurrentSignalStrength();
}
