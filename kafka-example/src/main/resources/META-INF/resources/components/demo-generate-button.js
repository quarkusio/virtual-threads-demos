import {LitElement, html, css} from 'lit';
import '@vaadin/icon';
import '@vaadin/button';
import '@vaadin/text-field';
import '@vaadin/text-area';
import '@vaadin/form-layout';
import '@vaadin/progress-bar';
import '@vaadin/checkbox';
import {until} from 'lit/directives/until.js';
import '@vaadin/grid';
import {columnBodyRenderer} from '@vaadin/grid/lit.js';
import '@vaadin/grid/vaadin-grid-sort-column.js';

export class DemoGenerateButton extends LitElement {
    static styles = css`
      .button {
        margin-right: 5px;
        margin-bottom: 5px;
        cursor: pointer;
      }
    `;

    constructor() {
        super();
    }

    render() {
        return html`
            <div class="button">
                <vaadin-button @click=${() => this._generate()} class="button primary">
                        Generate transactions
                </vaadin-button>
            </div>`;
    }

    _generate() {
        fetch("/transactions?count=2000", {
            method: "GET",
            headers: {
                "Content-Type": "application/json",
            },
        });
    }


}

customElements.define('demo-generate-button', DemoGenerateButton);