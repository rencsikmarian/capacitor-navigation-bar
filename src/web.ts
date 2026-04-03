import { WebPlugin } from '@capacitor/core';

import type { NavigationBarPlugin } from './definitions';

export class NavigationBarWeb extends WebPlugin implements NavigationBarPlugin {
  async show(): Promise<void> {
    console.log('Navigation Bar Showed!');
  }

  async hide(): Promise<void> {
    console.log('Navigation Bar Hidden!');
  }

  async setColor(options: { color: string; darkButtons?: boolean }): Promise<void> {
    console.log(
      `Navigation Bar color changed to ${options.color || '#FFFFFF'} : Dark Buttons: ${options.darkButtons ? 'YES' : 'NO'}`,
    );
  }

  async setTransparency(options: { isTransparent: boolean }): Promise<void> {
    console.log(`Navigation Bar is transparent: ${options.isTransparent ? 'YES' : 'NO'}`);
  }

  async getColor(): Promise<{ color: string }> {
    return { color: '#FFFFFF' };
  }
}
