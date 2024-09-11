import React from 'react';
import logo from './hhpnz.png';
import './App.css';
import Page1 from './page1';
import TopBar from './TopBar';
import Page2 from './page2';

function App() {
  const styles = {
    appContainer: {
      display: 'flex',
      flexDirection: 'column',
      height: '100vh',
      overflow: 'hidden',
    },
    contentContainer: {
      flexGrow: 1,
      overflowY: 'auto',
      paddingTop: '60px', // Adjust this value to match the height of your TopBar
    },
  };

  return (
    <div style={styles.appContainer}>
      <TopBar logo={logo} />
      <div style={styles.contentContainer}>
        <Page1 />
      </div>
    </div>
  );
}

export default App;