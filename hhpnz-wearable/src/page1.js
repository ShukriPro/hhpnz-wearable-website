import React, { useState, useEffect } from 'react';

const Page1 = () => {
    const [windowWidth, setWindowWidth] = useState(window.innerWidth);
    const [isHovered, setIsHovered] = useState(false);

    useEffect(() => {
        const handleResize = () => setWindowWidth(window.innerWidth);
        window.addEventListener('resize', handleResize);
        return () => window.removeEventListener('resize', handleResize);
    }, []);

    const styles = {
        pageContainer: {
            display: 'flex',
            flexDirection: windowWidth > 500 ? 'row' : 'column',
            justifyContent: 'space-between',
            alignItems: 'center',
            height: '100vh',
            width: '100%',
            overflow: 'hidden',
        },
        imageContainer: {
            flex: 1,
            display: 'flex',
            justifyContent: 'center',
            alignItems: 'center',
            height: '100%',
        },
        appImage: {
            width: '100%',
            height: 'auto',
            maxWidth: '800px',
        },
        content: {
            padding: '0 50px',
            margin: '0 auto',
            flex: 1,
            zIndex: 2,
            display: 'flex',
            flexDirection: 'column',
            justifyContent: 'center',
        },
        heading: {
            fontSize: '32px',
        },
        subtitle: {
            marginTop: '10px',
        },
        downloadButton: {
            padding: '12px 24px',
            backgroundColor: isHovered ? '#e07526' : '#f58231',
            color: 'white',
            border: 'none',
            borderRadius: '25px',
            fontSize: '16px',
            fontWeight: 'bold',
            cursor: 'pointer',
            marginTop: 20,
            alignSelf: 'flex-start',
            display: 'flex',
            alignItems: 'center',
            gap: '8px',
            transition: 'all 0.3s ease',
            boxShadow: isHovered 
                ? '0 4px 8px rgba(0,0,0,0.3)' 
                : '0 2px 5px rgba(0,0,0,0.2)',
            transform: isHovered ? 'translateY(-2px)' : 'translateY(0)',
        },
        downloadIcon: {
            marginLeft: '4px',
            transition: 'all 0.3s ease',
            transform: isHovered ? 'translateY(2px)' : 'translateY(0)',
        },
    };

    return (
        <>
            <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css" />
            <div style={styles.pageContainer}>
                <div style={styles.content}>
                    <h1 style={styles.heading}>Wearble Application</h1>
                    <p style={styles.subtitle}>
                        Next-gen health app that integrates data from Health Connect.
                    </p>
                    <button 
                        style={styles.downloadButton}
                        onMouseEnter={() => setIsHovered(true)}
                        onMouseLeave={() => setIsHovered(false)}
                    >
                        Download
                        <i className="fa fa-download" style={styles.downloadIcon}></i>
                    </button>
                </div>
                <div style={styles.imageContainer}>
                    <img
                        src={require('./App.gif')}
                        alt="App interface on smartphone"
                        style={styles.appImage}
                    />
                </div>
            </div>
        </>
    );
};

export default Page1;