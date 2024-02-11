import * as React from 'react';

import { StyleSheet, View, Text, Button } from 'react-native';
import { getCurrentSignalStrength } from 'react-native-android-signal-strength';

export default function App() {
  const [result, setResult] = React.useState<number | undefined>();

  React.useEffect(() => {
    getCurrentSignalStrength().then(setResult);
  }, []);

  const refreshSignalStrength = () => {
    getCurrentSignalStrength().then(setResult);
  };

  return (
    <View style={styles.container}>
      <Text>Result: {result}</Text>
      <Button onPress={refreshSignalStrength} title="Refresh Signal Strength" />
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
  },
  box: {
    width: 60,
    height: 60,
    marginVertical: 20,
  },
});
