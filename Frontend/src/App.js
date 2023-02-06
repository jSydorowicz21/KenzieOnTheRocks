import React from 'react';
import { Amplify } from 'aws-amplify';
import awsconfig from './aws-exports';
import {Authenticator , withAuthenticator } from '@aws-amplify/ui-react';

Amplify.configure(awsconfig)

function App(){
    return (
    <div className = "App">
        <header className = "App-header">
            <Authenticator />
            <h2>My App Content</h2>
        </header>
    </div>
    );
}

export default withAuthenticator(App);