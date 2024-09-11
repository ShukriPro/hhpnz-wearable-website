import React from 'react';

const TopBar = ({ logo }) => {
    const styles = {
        topBar: {
            position: 'fixed',
            top: 0,
            left: 0,
            width: '100%',
            height: '60px',
            backgroundColor: '#ffffff',
            boxShadow: '0 2px 4px rgba(0,0,0,0.1)',
            display: 'flex',
            justifyContent: 'center',
            alignItems: 'center',
            zIndex: 1000,
        },
        logo: {
            height: '40px',
            width: 'auto',
        },
    };

    return (
        <div style={styles.topBar}>
            <img src={logo} alt="HHPNZ Logo" style={styles.logo} />
        </div>
    );
};

export default TopBar;