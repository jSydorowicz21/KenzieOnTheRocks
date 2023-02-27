import { Amplify } from 'aws-amplify';
import { BrowserRouter, Routes, Route } from "react-router-dom";
import { Authenticator } from '@aws-amplify/ui-react';
import '@aws-amplify/ui-react/styles.css';
import { Auth } from 'aws-amplify';
import awsExports from './aws-exports';

Amplify.configure(awsExports);
Auth.configure(awsExports);

export default function App() {
    return (
        <Authenticator>
            {({ signOut, user }) => (
                <main>
                    <h1>Hello {user.username}</h1>
                    if(user.username != null) {
                       window.location.href = "index.html"
                    }
                    <button onClick={signOut}>Sign out</button>
                </main>
            )}
        </Authenticator>
    );
}