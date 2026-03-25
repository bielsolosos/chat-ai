import { Routes } from '@angular/router';

export const routes: Routes = [
  { path: '', redirectTo: 'chat', pathMatch: 'full' },
  { path: 'chat', loadChildren: () => import('./pages/chat/chat.routes').then(m => m.CHAT_ROUTES) }
];
